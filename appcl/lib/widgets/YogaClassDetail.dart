import 'package:flutter/material.dart';
import '../models/YogaClass.dart';

  class YogaClassDetail extends StatelessWidget {
  final YogaClass yogaClassData;

  const YogaClassDetail({Key? key, required this.yogaClassData}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              height: 25,
              width: 300,
              child: Text(
                yogaClassData.classTitle,
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 20,
                ),
              ),
            ),
            Container(
              margin: EdgeInsets.only(top: 10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text("Type: ${yogaClassData.classType}",
                            style: TextStyle(fontSize: 16),),
                      Text(
                        "Day in Week: ${yogaClassData.day}",
                          style: TextStyle(fontSize: 16),
                          )

                    ],
                  ),
                  SizedBox(height: 10),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        "Time: ${yogaClassData.time}",
                        style: TextStyle(fontSize: 16),
                      ),
                      Text(
                        "Duration: ${yogaClassData.duration}",
                        style: TextStyle(fontSize: 16),
                      ),
                    ],
                  ),
                  SizedBox(height: 10),
                  Text(
                    "Description: ${yogaClassData.description} ",
                    style: TextStyle(fontSize: 16),
                  ),
                  SizedBox(height: 10),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
