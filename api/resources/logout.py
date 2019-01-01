from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_jwt_extended import jwt_required, get_raw_jwt, get_jwt_identity
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
        emp_id = get_jwt_identity()

        last_login_entry = models.Employees_Logins.query.filter_by(Emp_id=emp_id).first()
        db.session.commit()

        return {"message": "Successfully logged out"}, 200