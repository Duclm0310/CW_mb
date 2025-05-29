import 'package:flutter/material.dart';

import '../models/user.dart';
import '../services/user_service.dart';


class UserScreen extends StatefulWidget {
  @override
  _UserScreenState createState() => _UserScreenState();
}

class _UserScreenState extends State<UserScreen> {
  final UserService _userService = UserService();
  List<User> _users = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _fetchUsers();
  }

  Future<void> _fetchUsers() async {
    try {
      List<User> users = await _userService.getUsers();
      setState(() {
        _users = users;
        _isLoading = false;
      });
    } catch (e) {
      print("Error fetching users: $e");
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Users List'),
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : _users.isEmpty
          ? Center(child: Text('No users found'))
          : ListView.builder(
        itemCount: _users.length,
        itemBuilder: (context, index) {
          User user = _users[index];
          return ListTile(
            title: Text(user.name),
            subtitle: Text(user.email),
            trailing: Text(user.role),
          );
        },
      ),
    );
  }
}
