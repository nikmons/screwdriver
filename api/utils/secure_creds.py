from bcrypt import hashpw, checkpw, gensalt

def get_hashed_password(plaintext_pwd):
    return hashpw(plaintext_pwd, gensalt())

def check_password(plaintext_pwd, hashed_pwd):
    return checkpw(plaintext_pwd, hashed_pwd)
