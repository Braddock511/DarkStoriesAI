# DarkStoriesAI
The "DarkStoriesAI" application is game where the player can play "Dark Stories" with AI. The AI generates scenarios for the player to solve, and the player asks yes or no questions. If he already knows the answer, he can describe what happened and if he is right, he wins the game.

# Interface preview
![alt text](https://ik.imagekit.io/jhddvvyeg/mobile-preview.png?updatedAt=1701618678298)

# Configuration
1. Run the following command to ingest all the data.
```shell 
python localGPT/scripts/ingest.py
```
2. Run the following command to launch API.
```shell
run - python localGPT/scripts/run_localGPT_API.py
```
3. API should run on http://0.0.0.0:5110 
4. You can run the interface in Android Studio

# Used tools
- Android Studio
- LocalGPT (https://github.com/PromtEngineer/localGPT)