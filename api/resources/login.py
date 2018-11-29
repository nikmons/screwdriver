from flask import Flask, jsonify, abort, make_response, session
from flask_restful import Api, Resource, reqparse, fields, marshal

from app import auth, db
from models import models

class LoginAPI(Resource):
    decorators = [auth.login_required]
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
        newUser = models.Employees.query.filter_by(Emp_Username = args['username']).first()
        if newUser:
            if newUser.Emp_Password == args['password']:
                for username in session['Users']:
                    if username == args['username']:
                        return "logged"
                    else:
                        session['Users'].append(args['username'])
                        return "just logged"
            else:
                return "denied"
        else:
            return "not found"
