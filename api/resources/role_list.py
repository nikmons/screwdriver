from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

role_fields = {
    'Role_id': fields.Integer,
    'Role_Name': fields.String,
    'Role_Description': fields.String,
}

class RoleListAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Role_Name', type=str, default="",
                                    location='json')
        self.reqparse.add_argument('Role_Description', type=str, default="",
                                   location='json')

        super(RoleListAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/roles_get.yml")
    def get(self):
        roles = models.Roles.query.all() #Query database
        print(roles)
        return {'Roles': [marshal(role, role_fields) for role in roles]}

    @jwt_required
    @swag_from("apidocs/roles_post.yml")
    def post(self):
        args = self.reqparse.parse_args()
        print(args)
        role = models.Roles(Role_Name=args["Role_Name"],
                                    Role_Description=args["Role_Description"])
        db.session.add(role)
        db.session.commit()
