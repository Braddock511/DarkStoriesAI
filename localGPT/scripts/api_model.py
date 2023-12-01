from pydantic import BaseModel

class UserPrompt(BaseModel):
    user_prompt: str
    session_id: str
    
class SessionId(BaseModel):
    session_id: str
    