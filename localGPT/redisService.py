import os
import sys
import logging
import redis
from typing import Optional, List

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

    def set_value(self, key: str, value: str) -> bool:
        try:
            return self.redis_client.set(key, value)
        except Exception as e:
            self.logger.error(f"Error setting value: {str(e)}")
            return False

    def get_value(self, key: str) -> Optional[str]:
        try:
            value = self.redis_client.get(key)
            if value is not None:
                return value.decode('utf-8')
            else:
                return None
        except Exception as e:
            self.logger.error(f"Error getting value: {str(e)}")
            return None

    def set_dict(self, key: str, dictionary: dict) -> bool:
        try:
            for field, value in dictionary.items():
                self.redis_client.hset(key, field, value)
            return True
        except Exception as e:
            self.logger.error(f"Error adding dictionary to hash: {str(e)}")
            return False

    def get_dict(self, key: str) -> dict:
        try:
            redis_dict = self.redis_client.hgetall(key)
            return {k.decode('utf-8'): v.decode('utf-8') for k, v in redis_dict.items()}
        except Exception as e:
            self.logger.error(f"Error getting dictionary from hash: {str(e)}")
            return {}
        
    def set_list(self, key: str, values: List[str]) -> bool:
        try:
            # Use the RPUSH command to append values to the list
            return self.redis_client.rpush(key, *values) > 0
        except Exception as e:
            self.logger.error(f"Error setting list: {str(e)}")
            return False

    def get_list(self, key: str) -> Optional[List[str]]:
        try:
            # Use the LRANGE command to retrieve the entire list
            values = self.redis_client.lrange(key, 0, -1)
            if values:
                return [v.decode('utf-8') for v in values]
            else:
                return None
        except Exception as e:
            self.logger.error(f"Error getting list: {str(e)}")
            return None    
    
    def delete_key(self, key: str) -> bool:
        try:
            return self.redis_client.delete(key)
        except Exception as e:
            self.logger.error(f"Error deleting key: {str(e)}")
            return False
        