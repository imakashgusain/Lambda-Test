# AWS Lambda Hello World - EventBridge Trigger

Simple boilerplate Java 21 project for AWS Lambda triggered by Amazon EventBridge with SLF4J console logging.

## Project Structure

```
Lambda-Test/
├── pom.xml                                  # Maven configuration (Java 21)
├── src/main/java/com/example/
│   └── HelloWorldHandler.java               # Lambda handler with @Slf4j
├── src/main/resources/
│   └── logback.xml                          # SLF4J Logback configuration (console output)
└── README.md                                # This file
```

## Prerequisites

- Java 21
- Maven 3.8+
- AWS CLI configured with appropriate credentials
- AWS Account

## Build

```bash
mvn clean package
```

Output JAR: `target/lambda-eventbridge-hello-world-1.0.0.jar`

## Deploy

### 1. Build the Project

```bash
mvn clean package
```

Output JAR: `target/lambda-eventbridge-hello-world-1.0.0.jar`

### 2. Create IAM Role (Console)

1. AWS Console → IAM → Roles → Create role
2. Choose: AWS service → Lambda
3. Add permissions: CloudWatch Logs (CreateLogGroup, CreateLogStream, PutLogEvents)
4. Role name: `lambda-hello-world-role`

### 3. Create Lambda Function (Console)

1. AWS Console → Lambda → Create function
2. Choose: Author from scratch
3. Function name: `hello-world-lambda`
4. Runtime: Java 21
5. Execution role: `lambda-hello-world-role`
6. Click Create function

### 4. Upload JAR (Console)

1. In Lambda function page → Code section
2. Click Upload from → File
3. Select: `target/lambda-eventbridge-hello-world-1.0.0.jar`
4. Wait for upload to complete

### 5. Set Handler (Console)

Lambda automatically detects the handler from the JAR manifest (configured in pom.xml).

**Handler:** `com.example.HelloWorldHandler` (pre-configured in pom.xml, no manual step needed)

### 6. Test (Console)

1. Click Test button
2. Use sample event from `examples/eventbridge-event.json`
3. Click Test
4. Response: "Hello World - akash" ✅

### 7. Add EventBridge Trigger (Console)

1. Click Add trigger
2. Choose: EventBridge (CloudWatch Events)
3. Create new rule:
   - Rule name: `hello-world-rule`
   - Schedule expression: `rate(5 minutes)`
   - State: Enabled
4. Click Add

### 8. View Logs (Console)

1. Monitor tab → View CloudWatch logs
2. Click latest log stream
3. See console output: "Hello World"

## SLF4J Logging Configuration

The project uses SLF4J with Logback for console logging:

**logback.xml:**
```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <target>System.out</target>
    <encoder>
        <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
</appender>

<root level="INFO">
    <appender-ref ref="CONSOLE"/>
</root>
```

All output goes directly to console (System.out).

## IAM Permissions

The Lambda execution role requires:
- `logs:CreateLogGroup` - Create CloudWatch log group
- `logs:CreateLogStream` - Create log stream
- `logs:PutLogEvents` - Write log events

See `examples/iam-lambda-policy.json` for the full policy.

## EventBridge Integration

EventBridge triggers Lambda on a schedule. Sample event in `examples/eventbridge-event.json`:

```json
{
  "version": "0",
  "id": "12345678-1234-1234-1234-123456789012",
  "detail-type": "myDetailType",
  "source": "mySource",
  "account": "123456789012",
  "time": "2026-02-10T13:45:00Z",
  "region": "us-east-1",
  "resources": [],
  "detail": {
    "message": "Hello from EventBridge"
  }
}

```

## Test Locally (AWS Lambda Runtime Emulator)

```bash
# Test with sample event
aws lambda invoke \
  --function-name hello-world-lambda \
  --payload file://examples/eventbridge-event.json \
  response.json

cat response.json
```

## View Logs

```bash
aws logs tail /aws/lambda/hello-world-lambda --follow
```


## Notes

- Lambda runtime automatically provides SLF4J logger via Logback
- Uses Lombok `@Slf4j` annotation for automatic logger field generation
- Logback configured to output to console (System.out)
- JAR built with Maven Shade plugin (fat JAR with all dependencies)
- Java 21 features available for use
- Minimal dependencies: AWS Lambda Core/Events, SLF4J, Logback, Lombok

## Dependencies

- `aws-lambda-java-core` (1.2.3) - Lambda runtime API
- `aws-lambda-java-events` (3.13.0) - Event types
- `org.slf4j:slf4j-api` (2.0.11) - SLF4J logging interface
- `ch.qos.logback:logback-classic` (1.4.14) - SLF4J implementation with console output
- `org.projectlombok:lombok` (1.18.30) - `@Slf4j` annotation for logger generation
```