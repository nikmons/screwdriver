from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

customer_fields = {
    'Cust_id': fields.Integer,
    'Cust_Created': fields.DateTime,
    'Cust_First_Name': fields.String,
    'Cust_Last_Name': fields.String,
    'Cust_Address_Name': fields.String,
    'Cust_Email': fields.String,
    'Cust_Contact_Num': fields.String,
    'Cust_Contact_Num_2': fields.String,
    'Cust_Birth_Date': fields.DateTime,
}

class CustomerListAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Cust_Created', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Cust_First_Name', type=str, default="",
                                    location='json')
        self.reqparse.add_argument('Cust_Last_Name', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Cust_Address_Name', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Cust_Email', type=int, default=0,
                                   location='json')
        self.reqparse.add_argument('Cust_Contact_Num', type=str, default=0,
                                   location='json')
        self.reqparse.add_argument('Cust_Contact_Num_2', type=str, default=0,
                                   location='json')
        self.reqparse.add_argument('Cust_Birth_Date', type=int, default=0,
                                   location='json')

        super(CustomerListAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/customers_get.yml")
    def get(self):
        customers = models.Customers.query.all() #Query database
        print(customers)
        return {'Customers': [marshal(customer, customer_fields) for customer in customers]}

    @jwt_required
    @swag_from("apidocs/customers_post.yml")
    def post(self):
        args = self.reqparse.parse_args()
        print(args)
        customer = models.Devices(Cust_Created=args["Cust_Created"],Cust_First_Name=args["Cust_First_Name"],
                                    Cust_Last_Name=args["Cust_Last_Name"], Cust_Address_Name=args["Cust_Address_Name"],
                                    Cust_Email=args["Cust_Email"], Cust_Contact_Num=args["Cust_Contact_Num"],
                                    Cust_Contact_Num_2=args["Cust_Contact_Num_2"], Cust_Birth_Date=args["Cust_Birth_Date"])
        db.session.add(customer)
        db.session.commit()