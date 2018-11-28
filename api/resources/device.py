from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal

from app import auth, db
from models import models

class DeviceAPI(Resource):
    decorators = [auth.login_required]

    def __init__(self):
        super(DeviceAPI, self).__init__()

    # Consider Device deletion side-effects!
    def delete(self, id):
        """
        file: apidocs/device_delete.yml
        """
        
        print("Delete id = {}".format(id))
        device = models.Devices.query.get(id)
        print(device)
        if device is None:
            abort(404)
        db.session.delete(device)
        db.session.commit()
        return {'result': True}