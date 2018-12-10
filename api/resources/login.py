from flask import Flask, jsonify, abort, make_response, session
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from

from flask_jwt_extended import (
    create_access_token,
    create_refresh_token
)
from werkzeug.security import safe_str_cmp

from app import db, jwt
from models import models

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
        if user and safe_str_cmp(user.Emp_Password, args['password']):
            # when authenticated, return a fresh access token and a refresh token
            access_token = create_access_token(identity=user.Emp_id, fresh=True)
            refresh_token = create_refresh_token(user.Emp_id)
            return {
                'message':'user_authenticated',
                'user':args['username'],
                'access_token': access_token,
                'refresh_token': refresh_token
            }, 200

        return {"message": "Invalid Credentials!"}, 401
