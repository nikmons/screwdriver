from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

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