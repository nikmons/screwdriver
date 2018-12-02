from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flask_login import login_required

from app import db
from models import models

class EmployeeAPI(Resource):
    decorators = [login_required]

    def __init__(self):
        super(EmployeeAPI, self).__init__()

    def delete(self, id):
        """
        file: apidocs/employee_delete.yml
        """
        print("Delete id = {}".format(id))
        employee = models.Employees.query.get(id)       
        print(employee) 
        if employee is None:
            abort(404)
        db.session.delete(employee)
        db.session.commit()
        return {'result': True}