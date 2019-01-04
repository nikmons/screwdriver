from flask import Flask, jsonify, abort, make_response, session
from flask_restful import Api, Resource, reqparse, marshal
from flasgger import swag_from
from utils.secure_creds import check_password

from flask_jwt_extended import (
    create_access_token,
    create_refresh_token
)
from werkzeug.security import safe_str_cmp

from app import db, jwt
from models import models

import datetime

class LoginAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', type=str, location='json')
        self.reqparse.add_argument('password', type=str, location='json')
        super(LoginAPI, self).__init__()

    @swag_from("apidocs/login_post.yml")
    def post(self):

        args = self.reqparse.parse_args()
        user =  models.Employees.query.filter_by(Emp_Username = str(args['username'])).first()
        print(args)
        if user and check_password(args['password'], user.Emp_Password):
            # when authenticated, return a fresh access token and a refresh token
            expires = datetime.timedelta(days=365)
            access_token = create_access_token(
                identity=user.Emp_id, fresh=True, expires_delta=expires)
            refresh_token = create_refresh_token(user.Emp_id)
            return {
                'message':'user_authenticated',
                'user':args['username'],
                'user_id': user.Emp_id,
                'access_token': access_token,
                'refresh_token': refresh_token
            }, 200
            login_entry = models.Employees_Logins(Emp_id=user.Emp_id, Emp_Logged_In=datetime.datetime.utcnow())
            db.session.add(login_entry)
            db.session.commit()

        return {"message": "Invalid Credentials!"}, 401
