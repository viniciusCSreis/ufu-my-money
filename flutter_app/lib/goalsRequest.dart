class GoalsRequest {
  final String value;
  final String description;
  final DateTime data;

  GoalsRequest({
    this.value,
    this.description,
    this.data,
  });

  Map<String, dynamic> toJson() => {
        'value': this.value,
        'description': this.description,
        'data': this.data.toIso8601String(),
      };
}
