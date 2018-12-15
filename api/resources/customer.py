from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

class CustomerAPI(Resource):

    def __init__(self):
        super(CustomerAPI, self).__init__()

    # Consider Customer deletion side-effects!
    @jwt_required
    @swag_from("apidocs/customer_delete.yml")
    def delete(self, id):        
        print("Delete id = {}".format(id))
        customer = models.Customers.query.get(id)
        print(customer)
        if customer is None:
            abort(404)
        db.session.delete(customer)
        db.session.commit()
        return {'result': True}

