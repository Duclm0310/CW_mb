import 'package:firebase_database/firebase_database.dart';
import '../models/user.dart';

class UserService {
  final DatabaseReference _dbRef = FirebaseDatabase.instance.ref("users");

  Future<List<User>> getUsers() async {
    DatabaseEvent event = await _dbRef.once();
    final data = event.snapshot.value;

    List<User> users = [];

    if (data is Map<dynamic, dynamic>) {
      data.forEach((key, value) {
        users.add(User.fromMap(Map<String, dynamic>.from(value)));
      });
    } else if (data is List) {
      for (var item in data) {
        if (item != null) {
          users.add(User.fromMap(Map<String, dynamic>.from(item)));
        }
      }
    }

    return users;
  }

  Future<User?> getUserById(String userId) async {
    DatabaseEvent event = await _dbRef.child(userId).once();
    if (event.snapshot.value != null) {
      return User.fromMap(Map<String, dynamic>.from(event.snapshot.value as Map));
    }
    return null;
  }


}