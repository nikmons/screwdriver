from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

class IssueAPI(Resource):

    def __init__(self):
        super(IssueAPI, self).__init__()

    @jwt_required
    @swag_from("apidocs/issue_put.yml")
    def put(self, id):
        print("Update id = {}".format(id))

    @jwt_required
    @swag_from("apidocs/issue_get.yml")
    def get(self, id):
        print("Get id = {}".format(id))
