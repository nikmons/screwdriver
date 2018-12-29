from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models
from utils.secure_creds import get_hashed_password

employee_fields = {
    'Emp_id': fields.Integer,
    'Emp_Created': fields.DateTime,
    'Emp_First_Name': fields.String,
    'Emp_Last_Name': fields.String,
    'Emp_Address_Name': fields.String,
    'Emp_Address_Num': fields.Integer,
    'Emp_Email': fields.String,
    'Emp_Contact_Num': fields.Integer,
    'Emp_Contact_Num2': fields.Integer,
    'Emp_Username': fields.String,
    'Emp_Password': fields.String
}

class EmployeeListAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Emp_First_Name', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Emp_Last_Name', type=str, default="",
                                    location='json')
        self.reqparse.add_argument('Emp_Address_Name', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Emp_Address_Num', type=int, default=0,
                                   location='json')
        self.reqparse.add_argument('Emp_Email', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Emp_Contact_Num', type=str, default=0,
                                   location='json')
        self.reqparse.add_argument('Emp_Contact_Num2', type=str, default=0,
                                   location='json')
        self.reqparse.add_argument('Emp_Username', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Emp_Password', type=str, default="",
                                   location='json')
        super(EmployeeListAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/employees_get.yml")
    def get(self):
        current_user = get_jwt_identity()
        print(current_user)
        employees = models.Employees.query.all() #Query database
        print(employees)
        return {'employees': [marshal(employee, employee_fields) for employee in employees]}

    @jwt_required
    @swag_from("apidocs/employees_post.yml")
    def post(self):
        current_user = get_jwt_identity()
        #print(current_user)
        args = self.reqparse.parse_args()
        print(args)

        existing_user = models.Employees.query.filter_by(Emp_Username=args["Emp_Username"]).first()
        print("Existing user = {}".format(existing_user))
        if existing_user is not None:
            return {"message" : "User '{}' already exists".format(args["Emp_Username"])}, 400

        employee = models.Employees(Emp_Created=None,Emp_First_Name=args["Emp_First_Name"],
                                    Emp_Last_Name=args["Emp_Last_Name"], Emp_Address_Name=args["Emp_Address_Name"],
                                    Emp_Address_Num=args["Emp_Address_Num"], Emp_Email=args["Emp_Email"],
                                    Emp_Contact_Num=args["Emp_Contact_Num"], Emp_Contact_Num2=args["Emp_Contact_Num2"],
                                    Emp_Username=args["Emp_Username"], Emp_Password=get_hashed_password(args["Emp_Password"]))
        db.session.add(employee)
        db.session.commit()
