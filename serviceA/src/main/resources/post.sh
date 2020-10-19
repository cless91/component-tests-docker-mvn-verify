curl -i -H "X-Correlation-ID: someCorrelationId" -H "Content-Type: application/json" \
  -d '{"name": "joseph2", "email":"toto@yopmail.com", "phoneNumber":"083665656"}' \
  http://localhost:8080/contacts
