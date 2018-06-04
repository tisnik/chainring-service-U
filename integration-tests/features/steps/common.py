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


@given('System is in initial state')
def initial_state(context):
    """Restart the system to the known initial state."""
    context.restart_system(context)


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


@then('I should get proper JSON data')
def check_json_data(context):
    """Check the data returned by the REST API."""
    assert context.response.json is not None


@then('I should find key {key} in received data')
def check_key_in_json(context, key):
    """Check the data returned by the REST API."""
    data = context.response.json()
    assert data is not None
    assert key in data, "key {key} can not be found in {keys}".format(key=key, keys=", ".join(data.keys()))


@then(u'I should find the value {value} under the key {key}')
def check_key_and_value_in_json(context, value, key):
    check_key_in_json(context, key)
    data = context.response.json()
    found = data[key]
    assert found == value, "expected '{expected}', but '{found}' was found instead".format(expected=value, found=found)

