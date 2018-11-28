from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal

from app import auth, db
from models import models

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
    decorators = [auth.login_required]

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
        self.reqparse.add_argument('Emp_Contact_Num', type=int, default=0,
                                   location='json')
        self.reqparse.add_argument('Emp_Contact_Num2', type=str, default=0,
                                   location='json')     
        self.reqparse.add_argument('Emp_Username', type=str, default="",
                                   location='json')     
        self.reqparse.add_argument('Emp_Password', type=str, default="",
                                   location='json')
        super(EmployeeListAPI, self).__init__()

    def get(self):
        """
        file: apidocs/employees_get.yml
        """
        employees = models.Employees.query.all() #Query database
        print(employees)
        return {'employees': [marshal(employee, employee_fields) for employee in employees]}

    def post(self):
        """
        file: apidocs/employees_post.yml
        """
        args = self.reqparse.parse_args()
        print(args)
        employee = models.Employees(Emp_Created=None,Emp_First_Name=args["Emp_First_Name"],
                                    Emp_Last_Name=args["Emp_Last_Name"], Emp_Address_Name=args["Emp_Address_Name"],
                                    Emp_Address_Num=args["Emp_Address_Num"], Emp_Email=args["Emp_Email"],
                                    Emp_Contact_Num=args["Emp_Contact_Num"], Emp_Contact_Num2=args["Emp_Contact_Num2"],
                                    Emp_Username=args["Emp_Username"], Emp_Password=args["Emp_Password"])
        db.session.add(employee)
        db.session.commit()