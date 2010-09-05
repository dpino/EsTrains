EsTrains is an Android application for querying Spanish Railway System time schedule. At this moment, it only supports national wide services.

Requests are queried to a remote service (http://pinowsky.appspot.com) which exposes an API for querying Spanish Railway System timetables. At the same time, this remote service queries Spanish Railway System trains via Renfe website (www.renfe.com). Renfe is the state-owned company which operates freight and passenger trains in Spain.

A query is defined by an origin, destination and date. Results are cached in DB for better performance being valid for one day.
