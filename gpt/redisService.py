import json
import logging
import sys
import redis
from typing import List

class RedisHandler:
    def __init__(self, host: str, port: int = 6379, db: int = 0):
        self.initialize_logging(logging.DEBUG)
        try:
            connection_pool = redis.ConnectionPool(host=host, port=port, db=db)
            self.redis_client = redis.StrictRedis(connection_pool=connection_pool)
        except Exception as e:
            self.logger.error(f"Error initializing Redis connection: {str(e)}")

    def initialize_logging(self, log_level):
        log_format = '%(levelname)-5s %(asctime)-5s %(name)-5s %(funcName)-5s line-%(lineno)-0d: %(message)s'
        self.logger = logging.getLogger(__name__)
        formatter = logging.Formatter(log_format)
        log_handler = logging.StreamHandler(sys.stdout)
        log_handler.setFormatter(formatter)
        self.logger.setLevel(log_level)
        self.logger.addHandler(log_handler)

    def set_value(self, key: str, value) -> bool:
        try:
            serialized_value = json.dumps(value)
            return self.redis_client.set(key, serialized_value)
        except Exception as e:
            self.logger.error(f"Error setting value: {str(e)}")
            return False

    def get_key(self, key: str):
        try:
            serialized_value = self.redis_client.get(key)
            if serialized_value is not None:
                return json.loads(serialized_value.decode('utf-8'))
            else:
                return None
        except Exception as e:
            self.logger.error(f"Error getting value: {str(e)}")
            return None

    def delete_key(self, key: str) -> bool:
        try:
            return self.redis_client.delete(key)
        except Exception as e:
            self.logger.error(f"Error deleting key: {str(e)}")
            return False


    def get_keys(self) -> List[str]:
        try:
            return [key.decode("utf-8") for key in self.redis_client.keys()]
        except Exception as e:
            self.logger.error(f"Error getting keys: {str(e)}")
            return []
