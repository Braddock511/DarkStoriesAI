from pydantic import BaseModel, Field
from enum import Enum

# GPT
class Story(BaseModel):
    topic: str = Field(description="topic of story")
    story: str = Field(description="actual story")
    solution: str = Field(description="solution of story")
    # fear: Enum = Field(description="user biggest fear")

class Answer(BaseModel):
    answer: str = Field(description="answer")
    
class Solution(BaseModel):
    answer: str = Field(description="answer")
    is_solved: bool = Field(description="user solved story?")

# API
class StoryRequest(BaseModel):
    story_format: str
    session_id: str

class UserPrompt(BaseModel):
    user_prompt: str
    session_id: str
    
class SessionId(BaseModel):
    session_id: str
    