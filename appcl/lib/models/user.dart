// models/user.dart

class User {
  String id;
  String email;
  String name;
  String password;
  String role;

  // Constructor
  User({
    required this.id,
    required this.email,
    required this.name,
    required this.password,
    required this.role,
  });


  factory User.fromMap(Map<String, dynamic> data) {
    return User(
      id: data['id'] ?? '',
      email: data['email'] ?? '',
      name: data['name'] ?? '',
      password: data['password'] ?? '',
      role: data['role'] ?? '',
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'email': email,
      'name': name,
      'password': password,
      'role': role,
    };
  }
}
