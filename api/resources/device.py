from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

from resources.fields import devices_fields

class DeviceAPI(Resource):

    def __init__(self):
        super(DeviceAPI, self).__init__()

    # Consider Device deletion side-effects!
    @jwt_required
    @swag_from("apidocs/device_delete.yml")
    def delete(self, id):
        print("Delete id = {}".format(id))
        device = models.Devices.query.get(id)
        print(device)
        if device is None:
            abort(404)
        db.session.delete(device)
        db.session.commit()
        return {'result': True}

    @jwt_required
    @swag_from("apidocs/device_get.yml")
    def get(self, id):
        print("Get id = {}".format(id))
        device = models.Devices.query.filter_by(Dev_id=id).first()
        print(device)        

        resp = marshal(device, devices_fields) #,200
        return resp, 200