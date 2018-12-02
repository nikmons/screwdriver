from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
import flask_login

from app import auth, db
from models import models

class LogoutAPI(Resource):
    
    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', required = True, type=str, location='json')
        super(LogoutAPI, self).__init__()


    def post(self):
        """
        file: apidocs/logout_post.yml
        """

        flask_login.logout_user()

        #TODO: Check if already logged in
        return "Logged out"