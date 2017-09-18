# Private security group; for now, only Kafka will
# live here
resource "aws_security_group" "private" {
    name = "vpc_private"
    description = "Allow incoming database connections."

    ingress { # Kafka
        from_port = 2181
        to_port = 2181
        protocol = "tcp"
        security_groups = ["${aws_security_group.public.id}"]
    }
    ingress { # Kafka
        from_port = 9092
        to_port = 9092
        protocol = "tcp"
        security_groups = ["${aws_security_group.public.id}"]
    }
    ingress {
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = ["${var.vpc_cidr}"]
    }
    ingress {
        from_port = -1
        to_port = -1
        protocol = "icmp"
        cidr_blocks = ["${var.vpc_cidr}"]
    }

    egress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    egress {
        from_port = 443
        to_port = 443
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    vpc_id = "${aws_vpc.default.id}"

    tags {
        Name = "Private Security Group"
    }
}

# username: bitnami
resource "aws_instance" "kafka-1" {
    ami = "${lookup(var.kafka-amis, var.aws_region)}"
    availability_zone = "${var.aws_availability_zone}"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    vpc_security_group_ids = ["${aws_security_group.private.id}"]
    subnet_id = "${aws_subnet.private.id}"
    source_dest_check = false

    tags {
        Name = "Kafka Server 1"
    }
}
