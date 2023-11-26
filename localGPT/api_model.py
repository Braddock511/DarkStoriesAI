from pydantic import BaseModel

class UserPrompt(BaseModel):
    user_prompt: str