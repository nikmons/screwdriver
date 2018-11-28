from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal

from app import auth, db
from models import models

class LogoutAPI(Resource):
    decorators = [auth.login_required]
    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', required = True, type=str, location='json')
        super(LogoutAPI, self).__init__()


    def post(self):
        """
        file: apidocs/logout_post.yml
        """

        args = self.reqparse.parse_args()
        for username in session['Users']:
            if username == args['username']:
                session.pop(args['username'], None)
                return "logged out"
        return "cant log out"