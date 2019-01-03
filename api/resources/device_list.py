from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

import datetime

from app import db
from models import models

from resources.fields import devices_fields

class DeviceListAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Dev_Manufacturer', type=str, default="",
                                    location='json')
        self.reqparse.add_argument('Dev_Model', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Dev_Model_Year', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Dev_Identifier_Code', type=str, default="",
                                   location='json')

        super(DeviceListAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/devices_get.yml")
    def get(self):
        devices = models.Devices.query.all() #Query database
        print(devices)
        return {'Devices': [marshal(device, devices_fields) for device in devices]}

    @jwt_required
    @swag_from("apidocs/devices_post.yml")
    def post(self):
        #Mipws prepei na checkaroume ti mas erxetai?

        args = self.reqparse.parse_args()
        print(args)
        device = models.Devices(Dev_Manufacturer=args["Dev_Manufacturer"],
                                    Dev_Model=args["Dev_Model"], Dev_Model_Year=args["Dev_Model_Year"],
                                    Dev_Identifier_Code=args["Dev_Identifier_Code"])
        db.session.add(device)
        db.session.commit()
