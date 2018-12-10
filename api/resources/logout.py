from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_jwt_extended import jwt_required, get_raw_jwt
from flasgger import swag_from

from app import db, blacklist
from models import models

class LogoutAPI(Resource):
    
    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', required = True, type=str, location='json')
        super(LogoutAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/logout_delete.yml")
    def delete(self):
        jti = get_raw_jwt()["jti"]
        blacklist.add(jti)
        return jsonify({"msg":"Successfully logged out"}), 200

    @jwt_required
    @swag_from("apidocs/logout_post.yml")
    def post(self):
        return "Old Logout - Does Nothing"