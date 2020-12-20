class GoalsResponse {
  final String id;
  final String value;
  final String description;
  final DateTime data;

  GoalsResponse({
    this.id,
    this.value,
    this.description,
    this.data,
  });

  factory GoalsResponse.fromJson(Map<String, dynamic> json) {
    return GoalsResponse(
      id: json["id"],
      value: json["value"].toString(),
      description: json["description"],
      data: DateTime.parse(json["data"]),
    );
  }
}
