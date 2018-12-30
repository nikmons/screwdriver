from flask import Flask, jsonify, abort, make_response
from flask_restful import Api, Resource, reqparse, fields, marshal
from flasgger import swag_from
from flask_jwt_extended import jwt_required, get_jwt_identity

from app import db
from models import models

timeline_fields = {
    'Issue_id': fields.Integer,
    'Emp_id': fields.Integer,
    'Act_id': fields.Integer,
    'Ist_Comment': fields.String,
    'Ist_Created': fields.DateTime
}

class IssueTimelineAPI(Resource):

    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('Act_id', type=int, default=1,
                                    location='json')
        self.reqparse.add_argument('Ist_Comment', type=str, default='Default Comment',
                                    location='json')   
        super(IssueTimelineAPI, self).__init__()


    @jwt_required
    @swag_from("apidocs/timeline_get.yml")
    def get(self, id):
        print("Get Timeline Entries for Issue = {}".format(id))

        entries = db.session.query(models.Issue_Timeline)       \
            .filter(models.Issue_Timeline.Issue_id == id)       \
            .order_by(models.Issue_Timeline.Ist_id.asc())       \
            .all()

        print(entries)
        return {'Entries': [marshal(entry, timeline_fields) for entry in entries]}
        
    @jwt_required
    @swag_from("apidocs/timeline_post.yml")
    def post(self, id):
        emp_id = get_jwt_identity()
        print("Enter new Timeline Entry for Issue = {}".format(id))
        args = self.reqparse.parse_args()
        print(args)
        timeline_entry = models.Issue_Timeline(Issue_id=id,       \
            Emp_id=emp_id, Act_id=args["Act_id"], Ist_Comment=args["Ist_Comment"])
        db.session.add(timeline_entry)
        db.session.commit()

        #Dont forget to change state!

    