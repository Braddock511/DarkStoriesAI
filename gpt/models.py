from pydantic import BaseModel, Field
from typing import Optional

# GPT
class Story(BaseModel):
    topic: str = Field(description="topic of story, related to actual story, don't write 'Dark Stories")
    story: str = Field(description="actual story")
    solution: str = Field(description="solution of story")

class Answer(BaseModel):
    answer: str = Field(description="answer")
    
class Solution(BaseModel):
    answer: str = Field(description="answer")
    solved: bool = Field(description="solved or no")

# API
class StoryRequest(BaseModel):
    story_format: str
    session_id: str
    user_id: str
    animal: str
    monster: str
    place: str

class UserPrompt(BaseModel):
    user_prompt: str
    session_id: str

class User(BaseModel):
    user_id: str
    name: Optional[str] = None
