# Provided in terraform.tfvars
variable "aws_access_key" {
    description = "AWS Access Key"
}
variable "aws_secret_key" {
    description = "AWS Secret Key"
}
variable "aws_key_name" {
    description = "Name of the SSH keypair to use in AWS."
}
variable "aws_key_path" {
    description = "Path to the private portion of the SSH key specified."
}

# Defined here
variable "aws_region" {
    description = "EC2 Region for the VPC"
    default = "us-east-1"
}

variable "aws_availability_zone" {
    description = "EC2 Availability zone for the VPC"
    default = "us-east-1a"
}

variable "amis" {
    type = "map"
    description = "Base Amazon Linux AMIs by region"
    default = {
        us-east-1 = "ami-4fffc834"
    }
}

# https://aws.amazon.com/marketplace/fulfillment?productId=5ab8e16a-7f76-42ee-a869-4c2ca41acc10&ref_=dtl_psb_continue&region=us-east-1
variable "kafka-amis" {
    description = "Pre-configured AMI to run kafka by region"
    default = {
        us-east-1 = "ami-603c3276"
    }
}

variable "nat-amis" {
    description = "Special ami preconfigured to do NAT"
    default = {
        us-east-1 = "ami-184dc970"
    }
}

variable "vpc_cidr" {
    description = "CIDR for the whole VPC"
    default = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
    description = "CIDR for the Public Subnet"
    default = "10.0.0.0/24"
}

variable "private_subnet_cidr" {
    description = "CIDR for the Private Subnet"
    default = "10.0.1.0/24"
}
