import logging
import os
import torch
import uvicorn

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse

from langchain.chains import RetrievalQA
from langchain.embeddings import HuggingFaceInstructEmbeddings
from langchain.vectorstores import Chroma

from load_models import load_model
from prompt_template_utils import get_prompt_template
from api_model import UserPrompt, SessionId
from redisService import RedisHandler
from constants import CHROMA_SETTINGS, EMBEDDING_MODEL_NAME, PERSIST_DIRECTORY, MODEL_ID, MODEL_BASENAME

if torch.backends.mps.is_available():
    DEVICE_TYPE = "mps"
elif torch.cuda.is_available():
    DEVICE_TYPE = "cuda"
else:
    DEVICE_TYPE = "cpu"

SHOW_SOURCES = True
logging.info(f"Running on: {DEVICE_TYPE}")
logging.info(f"Display Source Documents set to: {SHOW_SOURCES}")

EMBEDDINGS = HuggingFaceInstructEmbeddings(model_name=EMBEDDING_MODEL_NAME, model_kwargs={"device": DEVICE_TYPE})

# load the vectorstore
DB = Chroma(
    persist_directory=PERSIST_DIRECTORY,
    embedding_function=EMBEDDINGS,
    client_settings=CHROMA_SETTINGS,
)

RETRIEVER = DB.as_retriever()

LLM = load_model(device_type=DEVICE_TYPE, model_id=MODEL_ID, model_basename=MODEL_BASENAME)
prompt, memory = get_prompt_template(promptTemplate_type="llama", history=True)

QA = RetrievalQA.from_chain_type(
    llm=LLM,
    chain_type="stuff",
    retriever=RETRIEVER,
    return_source_documents=SHOW_SOURCES,
    chain_type_kwargs={
        "prompt": prompt,
        "memory": memory
    },
)

redis = RedisHandler("localhost")
app = FastAPI()

# CORS middleware to allow requests from a specific origin
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.post("/prompt")
async def prompt_route(request: UserPrompt):
    global QA
    user_prompt = request.user_prompt
    session_id = request.session_id  
    
    # Get the memory for this specific session
    memory_key = f"memory:{session_id}"
    memory = redis.get_list(memory_key)
    
    if user_prompt:
        res = QA(user_prompt, memory)
        answer, docs = res["result"], res["source_documents"]

        prompt_response_dict = {
            "Prompt": user_prompt,
            "Answer": answer,
        }

        prompt_response_dict["Sources"] = []
        for document in docs:
            prompt_response_dict["Sources"].append(
                (os.path.basename(str(document.metadata["source"])), str(document.page_content))
            )

        # Save prompt to memory
        redis.set_list(memory_key, user_prompt)  # assuming append_to_list is a method that adds to the list and manages its size

        return JSONResponse(content=prompt_response_dict)
    else:
        raise HTTPException(status_code=400, detail="No user prompt received")

@app.post("/clear-redis")
async def clear_redis(request: SessionId):
    try:
        session_id = request.session_id
        memory_key = f"memory:{session_id}"
        redis.delete_key(memory_key)

        return {"status": 200, "message": f"Redis memory cleared for session: {session_id}"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error: {e}")
        
if __name__ == "__main__":    
    uvicorn.run(app, host="0.0.0.0", port=5110)
