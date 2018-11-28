from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal

from app import auth, db
from models import models

devices_fields = {
    'Dev_id': fields.Integer,
    'Dev_Creator': fields.DateTime,
    'Dev_Manufacturer': fields.String,
    'Dev_Mode': fields.String,
    'Dev_Model_Year': fields.DateTime,
    'Dev_Identifier_Code': fields.String,
}

class DeviceListAPI(Resource):
    decorators = [auth.login_required]

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Dev_Creator', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Dev_Manufacturer', type=str, default="",
                                    location='json')
        self.reqparse.add_argument('Dev_Mode', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Dev_Model_Year', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Dev_Identifier_Code', type=int, default=0,
                                   location='json')

        super(DeviceListAPI, self).__init__()

    def get(self):
        """
        file: apidocs/devices_get.yml
        """
        devices = models.Devices.query.all() #Query database
        print(devices)
        return {'Devices': [marshal(device, devices_fields) for device in devices]}

    def post(self):
        """
        file: apidocs/devices_post.yml
        """

        #Mipws prepei na checkaroume ti mas erxetai?

        args = self.reqparse.parse_args()
        print(args)
        device = models.Devices(Dev_Creator=args["Dev_Creator"],Dev_Manufacturer=args["Dev_Manufacturer"],
                                    Dev_Model=args["Dev_Model"], Dev_Model_Year=args["Dev_Model_Year"],
                                    Dev_Identifier_Code=args["Dev_Identifier_Code"])
        db.session.add(device)
        db.session.commit()