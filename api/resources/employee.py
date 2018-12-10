from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

class EmployeeAPI(Resource):

    def __init__(self):
        super(EmployeeAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/employee_delete.yml")
    def delete(self, id):
        print("Delete id = {}".format(id))
        employee = models.Employees.query.get(id)       
        print(employee) 
        if employee is None:
            abort(404)
        db.session.delete(employee)
        db.session.commit()
        return {'result': True}