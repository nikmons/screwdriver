import os
import requests

def send_simple_message(track_num):
    api_key = os.getenv("MAILGUN_API_KEY")
    domain = os.getenv("MAILGUN_DOMAIN")

    return requests.post(
        "https://api.mailgun.net/v3/{}/messages".format(domain),
        auth=("api", "{}".format(api_key)),
        data={"from": "MobiRepair <mailgun@{}>".format(domain),
              "to": ["nikos13@hotmail.com"],
              "subject": "Hello",
              "text": "Your tracking number is {}".format(track_num)})
