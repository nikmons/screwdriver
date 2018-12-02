from flask import Flask, jsonify, abort, make_response, session
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_login import login_user

from app import auth, db
from models import models

class LoginAPI(Resource):
    
    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', type=str, location='json')
        self.reqparse.add_argument('password', type=str, location='json')
        super(LoginAPI, self).__init__()

    def post(self):
        """
        file: apidocs/login_post.yml
        """

        args = self.reqparse.parse_args()
        newUser = models.Employees.query.filter_by(Emp_Username = str(args['username'])).first()
        print(args)
        if newUser:
            if newUser.Emp_Password == str(args['password']):
                login_user(newUser)
                return "Logged In!"
            else:
                return "Access Denied. Wrong Credentials"
        else:
            return args
