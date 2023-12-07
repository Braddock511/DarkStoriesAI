import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware

from model_gpt import create_story, answer, solution
from models import StoryRequest, UserPrompt, SessionId
from redisService import RedisHandler

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

@app.post("/story")
async def story(request: StoryRequest):
    try:
        story_format = request.story_format
        session_id = request.session_id  
        
        story = create_story(story_format)
        story_key = f"story:{session_id}"
        redis.delete_key(story_key)
        redis.set_dict(story_key, dict(story))  

        return story
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed: {e}")

@app.post("/question")
async def question(request: UserPrompt):
    try:
        user_prompt = request.user_prompt
        session_id = request.session_id  
        
        # Get the story for this specific session
        story_key = f"story:{session_id}"
        story = redis.get_dict(story_key)

        # Save prompt to memory
        memory_key = f"memory:{session_id}"
        redis.set_list(memory_key, user_prompt)  

        llm_answer = answer(story["story"], user_prompt)
        
        return llm_answer
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed: {e}")

@app.post("/solve")
async def solve(request: UserPrompt):
    try:
        user_solution = request.user_prompt
        session_id = request.session_id
        
        # Get the story for this specific session
        story_key = f"story:{session_id}"
        story = redis.get_dict(story_key)
        
        llm_answer = solution(story["solution"], user_solution)
        
        return llm_answer
    except:
        raise HTTPException(status_code=500)

if __name__ == "__main__":    
    uvicorn.run(app, host="0.0.0.0", port=5110)