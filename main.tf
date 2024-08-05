provider "aws" {
  region = "us-east-1"
}

resource "aws_vpc" "my_vpc" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name = "my-vpc"
  }
}

resource "aws_subnet" "my_subnet" {
  vpc_id            = aws_vpc.my_vpc.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-east-1a"
  tags = {
    Name = "my-subnet"
  }
}

resource "aws_internet_gateway" "my_igw" {
  vpc_id = aws_vpc.my_vpc.id
}

resource "aws_instance" "my_instance1.1" {
  ami           = "ami-04a81a99f5ec58529"
  instance_type = "t2.micro"
  key_name      = "NNnn"
  subnet_id     = aws_subnet.my_subnet.id
  tags = {
    Name = "my-instance-1"
  }
}

resource "aws_instance" "my_instance1.2" {
  ami           = "ami-04a81a99f5ec58529"
  instance_type = "t2.medium"
  key_name      = "NNnn"
  subnet_id     = aws_subnet.my_subnet.id
  tags = {
    Name = "my-instance-2"
  }
}

resource "aws_instance" "my_instance1.3" {
  ami           = "ami-04a81a99f5ec58529"
  instance_type = "t3.medium"
  key_name      = "NNnn"
  subnet_id     = aws_subnet.my_subnet.id
  tags = {
    Name = "my-instance-3"
  }
}
