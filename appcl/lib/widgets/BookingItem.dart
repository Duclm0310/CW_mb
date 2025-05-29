import 'package:appcl/models/Booking.dart';
import 'package:appcl/services/class_instance_service.dart';
import 'package:flutter/material.dart';

import '../models/ClassInstance.dart';

class BookingItem extends StatefulWidget {
  final Booking booking;
  final VoidCallback onTap;

  const BookingItem({
    Key? key,
    required this.booking,
    required this.onTap,
  }) : super(key: key);


  @override
  State<BookingItem> createState() => _BookingItemState();
}

class _BookingItemState extends State<BookingItem> {
  final ClassInstanceService classInstanceService = ClassInstanceService();
  ClassInstance? classInstanceData;

  @override
  void initState() {
    super.initState();
    fetchClassInstanceData();
  }

  Future<void> fetchClassInstanceData() async {
    try {
      final data = await classInstanceService.getClassInstancesById(widget.booking.instanceId);
      setState(() {
        classInstanceData = data;
      });
    } catch (e) {
      print("Error fetching class instance data: $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: widget.onTap,
      child: Padding(
        padding: EdgeInsets.all(10),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.purple.shade50,
            borderRadius: BorderRadius.circular(16),
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Padding(
                padding: EdgeInsets.all(5),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      widget.booking.classTitle,
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    if (classInstanceData != null) ...[
                      Text("Instructor: ${classInstanceData!.userName}"),
                      Text("Date: ${classInstanceData!.formattedDate}"),
                    ] else
                      Text("Loading class details..."),
                  ],
                ),
              ),
              Container(
                padding: EdgeInsets.all(5),
                child: Text(widget.booking.status,
                style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold),),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
