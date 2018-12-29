from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

class RoleAPI(Resource):

    def __init__(self):
        super(RoleAPI, self).__init__()

    # Consider Customer deletion side-effects!
    @jwt_required
    @swag_from("apidocs/role_delete.yml")
    def delete(self, id):
        print("Delete id = {}".format(id))
        role = models.Roles.query.get(id)
        print(role)
        if role is None:
            abort(404)
        db.session.delete(role)
        db.session.commit()
        return {'result': True}
