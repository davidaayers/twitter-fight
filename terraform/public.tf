# Public security settings; if we add more servers
# here, we should set up multiple security groups per
# server type; keep it simple for now
resource "aws_security_group" "public" {
    name = "vpc_public"
    description = "Allow incoming HTTP connections."

    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    ingress {
        from_port = 443
        to_port = 443
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    ingress {
        from_port = 8080
        to_port = 8080
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    ingress {
        from_port = -1
        to_port = -1
        protocol = "icmp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    ingress {
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = ["${var.public_subnet_cidr}"]
    }
    ingress {
        from_port = -1
        to_port = -1
        protocol = "icmp"
        cidr_blocks = ["${var.public_subnet_cidr}"]
    }

    egress { #Kafka
        from_port = 2181 
        to_port = 2181
        protocol = "tcp"
        cidr_blocks = ["${var.private_subnet_cidr}"]
    }
    egress { #Kafka
        from_port = 9092 
        to_port = 9092
        protocol = "tcp"
        cidr_blocks = ["${var.private_subnet_cidr}"]
    }

    vpc_id = "${aws_vpc.default.id}"

    tags {
        Name = "Public Security Group"
    }
}

# username: ec2-user
resource "aws_instance" "spring-boot-1" {
    ami = "${lookup(var.amis, var.aws_region)}"
    availability_zone = "${var.aws_availability_zone}"
    instance_type = "t2.nano"
    key_name = "${var.aws_key_name}"
    vpc_security_group_ids = ["${aws_security_group.public.id}"]
    subnet_id = "${aws_subnet.public.id}"
    associate_public_ip_address = true
    source_dest_check = false

    tags {
        Name = "Spring Boot Server 1"
    }
}

# Elastic IP for external access
resource "aws_eip" "spring-boot-1" {
    instance = "${aws_instance.spring-boot-1.id}"
    vpc = true
}
