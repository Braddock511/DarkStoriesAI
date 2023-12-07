import os

from langchain.chat_models import ChatOpenAI
from langchain.prompts.chat import (
    ChatPromptTemplate,
    HumanMessagePromptTemplate,
)
from langchain.output_parsers import PydanticOutputParser
from models import Story, Answer, Solution
from dotenv import load_dotenv

load_dotenv()

OPENAI_MODEL = "gpt-3.5-turbo"
OPENAI_API_KEY = os.environ.get("OPENAI_API_KEY")

STORY_TEMPLATE = """
    {story_format}
    Follow task instructions strictly, respond only with a story in two sentences as the storyteller for 'Dark Stories' game, avoid unnecessary phrases. In this order:
    {format_instructions}
    """
    
ASK_TEMPLATE = """
    Answer Yes or No in correct language to this question {ask}, referring to the {story}.
    {format_instructions}    
"""

SOLUTION_TEMPLATE = """
    If this {user_solution} is equivalent to or demonstrates the same approach as this {solution}, write "You solved it, congratulations!" Otherwise, write "Something is missing, keep trying."
    {format_instructions}
"""
    
def create_story(story_format: str) -> Story:
    parser = PydanticOutputParser(pydantic_object=Story)

    llm = ChatOpenAI(openai_api_key=OPENAI_API_KEY, model_name=OPENAI_MODEL)
    message = HumanMessagePromptTemplate.from_template(
        template=STORY_TEMPLATE,
    )
    chat_prompt = ChatPromptTemplate.from_messages([message])

    chat_prompt_with_values = chat_prompt.format_prompt(
        story_format=story_format, format_instructions=parser.get_format_instructions()
    )
    output = llm(chat_prompt_with_values.to_messages()).content
    result = parser.parse(output)

    return result

def answer(story: str, ask: str) -> Answer:
    parser = PydanticOutputParser(pydantic_object=Answer)

    llm = ChatOpenAI(openai_api_key=OPENAI_API_KEY, model_name=OPENAI_MODEL)
    message = HumanMessagePromptTemplate.from_template(
        template=ASK_TEMPLATE,
    )
    chat_prompt = ChatPromptTemplate.from_messages([message])

    chat_prompt_with_values = chat_prompt.format_prompt(
        story=story, ask=ask, format_instructions=parser.get_format_instructions()
    )
    output = llm(chat_prompt_with_values.to_messages()).content
    result = parser.parse(output)

    return result

def solution(solution: str, user_solution: str) -> Solution:
    parser = PydanticOutputParser(pydantic_object=Answer)

    llm = ChatOpenAI(openai_api_key=OPENAI_API_KEY, model_name=OPENAI_MODEL)
    message = HumanMessagePromptTemplate.from_template(
        template=SOLUTION_TEMPLATE,
    )
    chat_prompt = ChatPromptTemplate.from_messages([message])

    chat_prompt_with_values = chat_prompt.format_prompt(
        solution=solution, user_solution=user_solution, format_instructions=parser.get_format_instructions()
    )
    output = llm(chat_prompt_with_values.to_messages()).content
    result = parser.parse(output)

    return result
