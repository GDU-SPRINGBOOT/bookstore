# Use node image as the base
FROM node:18-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the project (excluding files specified in .dockerignore)
COPY . .

# Expose port 3000 (matches typical React development server port)
EXPOSE 3000

# Start the React app in development mode
CMD [ "npm", "start" ]
