"""Common test steps and checks."""

import string
import datetime
import json
import time
import os
import re

from behave import given, then, when
from urllib.parse import urljoin
import requests


@when('I wait {num:d} seconds')
@then('I wait {num:d} seconds')
def pause_scenario_execution(context, num):
    """Pause the test for provided number of seconds."""
    time.sleep(num)


@given('System is running')
def running_system(context):
    """Ensure that the system is running, (re)start it if necesarry."""
    if not context.is_running(context):
        initial_state(context)


@when('I access the API endpoint {url}')
def access_endpoint(context, url):
    """Access the service API using the HTTP GET method."""
    context.response = requests.get(context.api_url + url)


@then('I should get {status:d} status code')
def check_status_code(context, status):
    """Check the HTTP status code returned by the REST API."""
    assert context.response.status_code == status
