# class TaskAPI(Resource):
#     decorators = [auth.login_required]

#     def __init__(self):
#         self.reqparse = reqparse.RequestParser()
#         self.reqparse.add_argument('title', type=str, location='json')
#         self.reqparse.add_argument('description', type=str, location='json')
#         self.reqparse.add_argument('done', type=bool, location='json')
#         super(TaskAPI, self).__init__()

#     def get(self, id):
#         """
#         file: apidocs/task_get.yml
#         """
#         task = [task for task in tasks if task['id'] == id]
#         if len(task) == 0:
#             abort(404)
#         return {'task': marshal(task[0], task_fields)}

#     def put(self, id):
#         task = [task for task in tasks if task['id'] == id]
#         if len(task) == 0:
#             abort(404)
#         task = task[0]
#         args = self.reqparse.parse_args()
#         for k, v in args.items():
#             if v is not None:
#                 task[k] = v
#         return {'task': marshal(task, task_fields)}

#     def delete(self, id):
#         task = [task for task in tasks if task['id'] == id]
#         if len(task) == 0:
#             abort(404)
#         tasks.remove(task[0])
#         return {'result': True}