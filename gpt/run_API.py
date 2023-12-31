import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware

from model_gpt import create_story, answer, solution
from models import StoryRequest, UserPrompt, User
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
        user_id = request.user_id
        animal = request.animal
        monster = request.monster
        place = request.place

        fears = [animal, monster, place]
        story = dict(create_story(story_format, fears))
        story_key = f"story:{session_id}"
        story_content = {"session_id": session_id, "user_id": user_id, "topic": story['topic'], "story": story['story'], "answers": [{"index": 0, "answer": story['story']}], "solution": story['solution']}
        redis.set_value(story_key, story_content)  
        
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
        story = redis.get_key(story_key)
        index = story["answers"][-1]["index"]+1
        user_ask = user_prompt.split(",")[0]
        story["answers"].append({"index": index, "header": "ASKER", "answer": user_ask})

        llm_answer = dict(answer(story["story"], user_prompt))
        story["answers"].append({"index": index+1, "header": "STORY TELLER", "answer": llm_answer["answer"]})
        
        redis.set_value(story_key, story)
        
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
        story = redis.get_key(story_key)
        index = story["answers"][-1]["index"]+1
        story["answers"].append({"index": index, "header": "ASKER", "answer": user_solution})

        llm_answer = dict(solution(story["solution"], user_solution))
        story["answers"].append({"index": index+1, "header": "STORY TELLER", "answer": llm_answer["answer"]})
        
        redis.set_value(story_key, story)
        
        return llm_answer
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed: {e}")

@app.post("/stories")
async def stories(request: User):
    try:
        user_id = request.user_id

        redis_keys = redis.get_keys()
        stories_content = [redis.get_key(key) for key in redis_keys if redis.get_key(key)['user_id']==user_id]

        return stories_content
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed: {e}")
            
@app.post("/set-user")
async def set_user(request: User):
    try:
        user_id = request.user_id
        name = request.name

        user_key = f"user:{user_id}"
        user_content = {"id": user_id, "name": name}
        redis.set_value(user_key, user_content)

        return user_content
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed: {e}")
 
@app.post("/get-user")
async def set_user(request: User):
    try:
        user_id = request.user_id
        user_key = f"user:{user_id}"
        
        return redis.get_key(user_key)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed: {e}")
        
if __name__ == "__main__":    
    uvicorn.run(app, host="0.0.0.0", port=5110)
