from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

state_fields = {
    'Stat_Name': fields.String,
    'Stat_Desc': fields.String,
}

class StatesListAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Stat_Id', type=int, default="",
                            location='json')
        self.reqparse.add_argument('Stat_Name', type=str, default="",
                                   location='json')
        self.reqparse.add_argument('Stat_Desc', type=str, default="",
                                    location='json')
        super(StatesListAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/states_get.yml")
    def get(self):
        states = models.State.query.all() #Query database
        print(states)
        return {'states': [marshal(state, state_fields) for state in states]}

    @jwt_required
    @swag_from("apidocs/states_post.yml")
    def post(self):
        args = self.reqparse.parse_args()
        print(args)
        state = models.State(Stat_Name=args["Stat_Name"], Stat_Desc=args["Stat_Desc"])
        db.session.add(state)
        db.session.commit()
