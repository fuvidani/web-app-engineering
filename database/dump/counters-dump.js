print('dump start');

db.counters.save(
    {
      "_id": "counter",
      "value": 0
    }
);

print('dump complete');