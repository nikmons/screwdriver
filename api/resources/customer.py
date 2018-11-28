from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal

from app import auth, db
from models import models

class CustomerAPI(Resource):
    decorators = [auth.login_required]

    def __init__(self):
        super(CustomerAPI, self).__init__()

    # Consider Customer deletion side-effects!
    def delete(self, id):
        """
        file: apidocs/customer_delete.yml
        """
        
        print("Delete id = {}".format(id))
        customer = models.Customers.query.get(id)
        print(customer)
        if customer is None:
            abort(404)
        db.session.delete(customer)
        db.session.commit()
        return {'result': True}

