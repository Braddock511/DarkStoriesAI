from pydantic import BaseModel, Field
from enum import Enum

# GPT
class Story(BaseModel):
    topic: str = Field(description="topic of story, related to actual story, don't write 'Dark Stories")
    story: str = Field(description="actual story")
    solution: str = Field(description="solution of story")
    # fear: Enum = Field(description="user biggest fear")

class Answer(BaseModel):
    answer: str = Field(description="answer")
    
class Solution(BaseModel):
    answer: str = Field(description="answer")
    solved: bool = Field(description="solved or no")

# API
class StoryRequest(BaseModel):
    story_format: str
    session_id: str

class UserPrompt(BaseModel):
    user_prompt: str
    session_id: str
    